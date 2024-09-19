# CNSoftBei2022
本项目为2022年“中国软件杯”大学生软件精英挑战赛A3赛题视障人士资讯辅助软件的Hi Info团队的作品存档

**软件结构：**
![image](https://github.com/user-attachments/assets/d0419846-7687-4d23-bc69-4fddbed0a0bd)

Hi Info系统技术架构由前端技术、后端技术、核心算法和数据库服务四个部分组成。前端技术通过微信小程序、安卓应用和浏览器网页四端为用户和管理员提供系统的可视化展示以及相应服务。后端技术选型为由java程序编写的Spring Boot框架为系统的核心部分，负责处理前端请求，整合了Redis中间件对数据进行缓存以提升效率，并与新闻推荐核心算法、MYSQL 数据库进行数据交互，存储和处理系统数据并调用算法完成前端的响应。

Python核心算法采用Yolo v4-tiny物体检测与识别框架，以OpenCV和TensorFlow框架进行实现，并通过导出的tf lite模型嵌入安卓端用作离线识别检测。数据库采用MYSQL数据库，用于数据的持久化。

**技术架构：**
![image](https://github.com/user-attachments/assets/2a2042b5-a20f-4f8b-91b5-3cae6d2cb103)
