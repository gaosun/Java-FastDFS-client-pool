# Java-FastDFS-client-pool

在happyfish100的基础上增加了trackerserver连接池以及心跳
使用方法：
1.初始化FileManage.initFastDFSPool();
2.获取实际处理类   IFileOperation ifO = FileManage.createtDefaultDevice();
3.调用实际方法： ifO.upload_file(filepath)
