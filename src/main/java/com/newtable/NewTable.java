package com.newtable;

import com.newtable.annotation.PrimaryKey;
import com.newtable.annotation.Table;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NewTable {
    private static Logger log = Logger.getLogger(NewTable.class); // 日志记录器

    private static List<Class> newC = new ArrayList<>(); // 存储找到的带有 @Table 注解的类
    private static DBHelper db = new DBHelper(); // 数据库操作类实例

    public static void createTable() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        String packageName = "com";
        String packagePath = System.getProperty("user.dir") + "\\src\\main\\java\\com";
        packagePath = packagePath.replaceAll("\\\\", "/");


        //查找此包下的文件
        findPackageClasses(packagePath, packageName);

        //目前我的规则是必须有@Table，才能创建表
        //@Field是用来指定名字的，没有就用默认名
        //@PrimaryKey用来指定主键
        String sql = "";

        for (Class<?> c : newC) {
            // 已经找到所有带@Table的类
            // 现在开始遍历每个类的每个属性并做好拼接
            Object o = c.newInstance();

            // 表名
            String tableName = ((Table) c.getAnnotation(Table.class)).value();
            if (tableName == null || "".equals(tableName)) {
                tableName = c.getSimpleName();
            }

            Field[] fields = c.getDeclaredFields();
            StringBuilder tableSql = new StringBuilder("CREATE TABLE " + tableName + " (");

            StringBuilder foreignKeys = new StringBuilder();

            for (Field field : fields) {
                // 判断是否有注解，没有就默认值
                Class<?> type = field.getType();

                // 属性名
                String fieldName = "";
                if (field.getAnnotation(com.newtable.annotation.Field.class) == null || "".equals(field.getAnnotation(com.newtable.annotation.Field.class).value())) {
                    // 说明没有指定值
                    fieldName = field.getName();
                } else {
                    fieldName = field.getAnnotation(com.newtable.annotation.Field.class).value();
                }

                String foreignTable = ""; // 对应的表
                String foreign = ""; // 外键字段名
                // 现在是外键
                if (field.getAnnotation(com.newtable.annotation.Field.class) != null && !"".equals(field.getAnnotation(com.newtable.annotation.Field.class).foreignKey())) {
                    // 说明有外键
                    String temp = field.getAnnotation(com.newtable.annotation.Field.class).foreignKey();
                    String[] split = temp.split("-");
                    foreignTable = split[0];
                    foreign = split[1];
                }

                if ("int".equals(type.getName()) || "java.lang.Integer".equals(type.getName()) || "byte".equals(type.getName()) || "java.lang.Byte".equals(type.getName()) || "short".equals(type.getName()) || "java.lang.Short".equals(type.getName())) {
                    if (field.getAnnotation(PrimaryKey.class) != null) {
                        tableSql.append(fieldName).append(" INT PRIMARY KEY AUTO_INCREMENT,");
                    } else {
                        tableSql.append(fieldName).append(" INT,");
                        if (!foreign.equals("")) {
                            foreignKeys.append("FOREIGN KEY (").append(fieldName).append(") REFERENCES ").append(foreignTable).append("(").append(foreign).append("),");
                        }
                    }
                } else if ("double".equals(type.getName()) || "java.lang.Double".equals(type.getName()) || "float".equals(type.getName()) || "java.lang.Float".equals(type.getName())) {
                    if (field.getAnnotation(PrimaryKey.class) != null) {
                        tableSql.append(fieldName).append(" DOUBLE PRIMARY KEY AUTO_INCREMENT,");
                    } else {
                        tableSql.append(fieldName).append(" DOUBLE,");
                        if (!foreign.equals("")) {
                            foreignKeys.append("FOREIGN KEY (").append(fieldName).append(") REFERENCES ").append(foreignTable).append("(").append(foreign).append("),");
                        }
                    }
                } else {
                    if (field.getAnnotation(PrimaryKey.class) != null) {
                        tableSql.append(fieldName).append(" VARCHAR(255) PRIMARY KEY AUTO_INCREMENT,");
                    } else {
                        tableSql.append(fieldName).append(" VARCHAR(255),");
                        if (!foreign.equals("")) {
                            foreignKeys.append("FOREIGN KEY (").append(fieldName).append(") REFERENCES ").append(foreignTable).append("(").append(foreign).append("),");
                        }
                    }
                }
            }

            // 移除最后一个逗号
            tableSql.setLength(tableSql.length() - 1);

            // 添加外键约束
            if (foreignKeys.length() > 0) {
                tableSql.append(", ").append(foreignKeys.toString().substring(0, foreignKeys.length() - 1));
            }

            tableSql.append(")");

            sql = tableSql.toString();
            System.out.println(sql);
            db.doUpdate(sql);
        }


    }


    private static void findPackageClasses(String packagePath, String packageName) throws UnsupportedEncodingException {
        if (packagePath.startsWith("/")) {
            packagePath = packagePath.substring(1); // 去掉路径开头的斜杠
        }
        packagePath = URLDecoder.decode(packagePath, "utf-8"); // 防止路径中文，统一转utf-8
        // 获取路径下所有的文件
        File file = new File(packagePath);
        File[] classFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {//过滤文件，只保留文件夹和java类
                return pathname.isDirectory() || pathname.getName().endsWith(".java"); // 过滤目录和 .java 文件
            }
        });

        if (classFiles != null && classFiles.length > 0) {
            for (File classFile : classFiles) {
                if (classFile.isDirectory()) {
                    findPackageClasses(classFile.getAbsolutePath(), packageName + "." + classFile.getName()); // 递归查找目录
                } else {
                    if (!classFile.getName().endsWith(".java")) {
                        continue; // 跳过非 .java 文件
                    }
                    // 使用类加载器加载 class 文件
                    URLClassLoader uc = new URLClassLoader(new URL[]{});
                    try {
                        //loadClass只能用java下面的包.xx来找想要的java类
                        Class cls = uc.loadClass(packageName + "." + classFile.getName().replace(".java", ""));
                        if (cls.getAnnotation(Table.class) != null) {
                            log.info(cls); // 记录找到的类
                            newC.add(cls); // 添加到列表
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // 处理异常
                    }
                }
            }
        }
    }
}