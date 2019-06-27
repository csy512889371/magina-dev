# 公共组件
1、spring-boot-starter-data-jpa （spring jpa 动态sql）



# git 使用方法

1.clone项目
* 选择项目导入目录，打开git bash here窗口
* 执行命令
> git clone http://192.168.111.123/jycj-code/rjsoft-jycj-admin-dev.git

2.创建本地分支
 * 进入项目目录
 * 创建本地（dev）分支
> git checkout -b dev 

3.拉取/更新远程（dev) 分支到本地（dev）分支
>  git pull origin dev

4.添加新增的代码到git管理
> git add .

5.提交更新代码到本地分支
> git commit -m "commit description "

6.提交本地分支到远程分支
* 执行前要先执行第三步，更新一下本地代码，以免冲突
>  git push origin dev

