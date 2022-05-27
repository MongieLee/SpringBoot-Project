# multiplayer-blogging-system
多人博客项目（MBS）
使用技术栈：
1. Spring boot
2. MyBatis
3. MySQL
4. Junit
5. Docker
此项目为前后端分离项目，该仓库为纯Java后端项目
### 启动项目
1. `docker run --name drbk -e MYSQL_ROOT_PASSWORD=12345678 -e MYSQL_DATABASE=drbk -d -p 3306:3306 mysql:5.7` 安装docker后，执行命令启动一个MYSQL的容器
2. 执行mvn flyway:migrate灌入数据
3. 启动Application类
