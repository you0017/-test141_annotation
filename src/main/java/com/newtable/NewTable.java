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
    private static Logger log = Logger.getLogger(NewTable.class);

    private static List<Class> newC = new ArrayList<>();
    private static DBHelper db = new DBHelper();
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

        for (Class c : newC) {
            //已经找到所有带@Table的类
            //现在开始遍历每个类的每个属性并做好拼接
            Object o = c.newInstance();

            //表名
            String tableName = ((Table) c.getAnnotation(Table.class)).value();
            if (tableName == null || "".equals(tableName)){
                tableName = c.getSimpleName();
            }


            Field[] fields = c.getDeclaredFields();
            sql = "create table "+tableName+"(";

            for (Field field : fields) {
                //判断是否有注解，没有就默认值
                Class<?> type = field.getType();

                //属性名
                String fieldName = "";
                if (field.getAnnotation(com.newtable.annotation.Field.class)==null||"".equals(field.getAnnotation(com.newtable.annotation.Field.class).value())){
                    //说明没有指定值
                    fieldName = field.getName();
                }else {
                    fieldName = field.getAnnotation(com.newtable.annotation.Field.class).value();
                }

                String foreign_table = "";//对应的表
                String foreign = "";
                //现在是外键
                if (field.getAnnotation(com.newtable.annotation.Field.class)!=null&&!"".equals(field.getAnnotation(com.newtable.annotation.Field.class).foreignKey())){
                    //说明有外键
                    String temp = field.getAnnotation(com.newtable.annotation.Field.class).foreignKey();
                    String[] split = temp.split("-");
                    foreign_table = split[0];
                    foreign = split[1];
                }


                if ("int".equals(type) || "java.lang.Integer".equals(type)||"byte".equals(type) || "java.lang.Byte".equals(type)||"short".equals(type) || "java.lang.Short".equals(type)) {
                    if (field.getAnnotation(PrimaryKey.class) != null) {
                        sql +=  fieldName + " int primary key,";
                    } else {
                        if (foreign.equals("")){
                            sql += fieldName + " int,";
                        }else {
                            sql += fieldName + " int references "+foreign_table+"("+foreign+"),";
                        }
                    }

                } else if ("double".equals(type) || "java.lang.Double".equals(type)||"float".equals(type) || "java.lang.Float".equals(type)) {
                    if (field.getAnnotation(PrimaryKey.class) != null) {
                        sql += fieldName + " double primary key,";
                    } else {
                        if (foreign.equals("")){
                            sql += fieldName + " double,";
                        }else {
                            sql += fieldName + " double references "+foreign_table+"("+foreign+"),";
                        }
                    }
                } else {
                    if (field.getAnnotation(PrimaryKey.class) != null) {
                        sql += fieldName + " varchar(255) primary key,";
                    } else {
                        if (foreign.equals("")){
                            sql += fieldName + " varchar(255),";
                        }else {
                            sql += fieldName + " varchar(255) references "+foreign_table+"("+foreign+"),";
                        }
                    }
                }
            }
            sql = sql.substring(0, sql.length() - 1)+")";
            System.out.println(sql);
        }


        db.doUpdate(sql);
    }

    private static void findPackageClasses(String packagePath, String packageName) throws UnsupportedEncodingException {
        if (packagePath.startsWith("/")) {
            packagePath = packagePath.substring(1);
        }
        packagePath = URLDecoder.decode(packagePath, "utf-8");
        //取这个路径下所有的文件
        File file = new File(packagePath);
        File[] classFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname instanceof Object || pathname.isDirectory()) {
                    return true;
                }else {
                    return false;
                }
            }
        });
        //System.out.println(classFiles);
        if (classFiles != null && classFiles.length > 0){
            for (File classFile : classFiles) {
                if (classFile.isDirectory()){
                    findPackageClasses(classFile.getAbsolutePath(), packageName + "." + classFile.getName());
                }else {
                    if (!classFile.getName().endsWith(".java")){
                        continue;
                    }
                    //是字节码文件·则利用类加载器加载class文件
                    URLClassLoader uc = new URLClassLoader(new URL[]{});
                    try {
                        Class cls = uc.loadClass(packageName + "." + classFile.getName().replaceAll(".java", ""));
                        Annotation[] annotations = cls.getAnnotations();
                        if (cls.getAnnotation(Table.class)!=null){
                            log.info(cls);
                            newC.add(cls);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
