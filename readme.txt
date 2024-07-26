1.add将源码或其他加入到git托管去

2.commit将git托管区中的文件提交到本地仓库
    commit时一定要提供一个信息

3.push将本地仓库中的代码提交到gitee中央服务器中，
    当前条件：gitee中已经有一个仓库
            本地有一个仓库

            所以会导致 push rejected

4.解决push rejected问题
    找到项目的目录 -》 进入git bash命令行
        输入： git pull origin master --allow-unrelated-histories
        输入： git push -u origin master -f

OK