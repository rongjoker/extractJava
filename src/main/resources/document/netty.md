## netty与网络编程

在java线程中，每个线程都有一个ThreadLocalMap实例变量,采用线性探测的方式解决hash冲突；
FastThreadLocal直接用数组的方式，实现o(1)的复杂度取值

<br>
Netty 的零拷贝主要包含三个方面：
Netty 的接收和发送 ByteBuffer 采用 DIRECT BUFFERS，使用堆外直接内存进行 Socket 读写，不需要进行字节缓冲区的二次拷贝。如果使用传统的堆内存（HEAP BUFFERS）进行 Socket 读写，JVM 会将堆内存 Buffer 拷贝一份到直接内存中，然后才写入 Socket 中。相比于堆外直接内存，消息在发送过程中多了一次缓冲区的内存拷贝。
Netty 提供了组合 Buffer 对象，可以聚合多个 ByteBuffer 对象，用户可以像操作一个 Buffer 那样方便的对组合 Buffer 进行操作，避免了传统通过内存拷贝的方式将几个小 Buffer 合并成一个大的 Buffer。
Netty 的文件传输采用了 transferTo 方法，它可以直接将文件缓冲区的数据发送到目标 Channel，避免了传统通过循环 write 方式导致的内存拷贝问题。

<br>

```

 try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            final ChannelPipeline pipeline = socketChannel.pipeline();
//                            pipeline.addLast(new ReadTimeoutHandler(5));//timeout->5s
                            pipeline.addLast(new HttpServerCodec());//以块的形式进行写
                            pipeline.addLast(new ChunkedWriteHandler());//http请求是分段处理的
                            pipeline.addLast(new HttpObjectAggregator(1024 << 10));
                            //心跳检测,读、写、读写,默认是TimeUnit.SECONDS,触发IdleStateEvent，传递给pipeline下一个handle的UserEventTrigger
//                            pipeline.addLast(new IdleStateHandler(5,10,15, TimeUnit.SECONDS));
                            pipeline.addLast(gatherHandler);

                        }
                    })

            ;

            channelFuture = serverBootstrap.bind(host, port).sync();

            log.info("server start :[{}]",port);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

```


### tomcat
<br>
假设来自客户的请求为：
http://localhost:8080/wsota/wsota_index.jsp
1. 请求被发送到本机端口8080，被在那里侦听的Coyote HTTP/1.1 Connector获得
2. Connector把该请求交给它所在的Service的Engine来处理，并等待来自Engine的回应
3. Engine获得请求localhost/wsota/wsota_index.jsp，匹配它所拥有的所有虚拟主机Host
4. Engine匹配到名为localhost的Host（即使匹配不到也把请求交给该Host处理，因为该Host被定义为该Engine的默认主机）
5. localhost Host获得请求/wsota/wsota_index.jsp，匹配它所拥有的所有Context
6. Host匹配到路径为/wsota的Context（如果匹配不到就把该请求交给路径名为”“的Context去处理）
7. path=”/wsota”的Context获得请求/wsota_index.jsp，在它的mapping table中寻找对应的servlet
8. Context匹配到URL PATTERN为*.jsp的servlet，对应于JspServlet类
9. 构造HttpServletRequest对象和HttpServletResponse对象，作为参数调用JspServlet的doGet或doPost方法
10. Context把执行完了之后的HttpServletResponse对象返回给Host
11. Host把HttpServletResponse对象返回给Engine
12. Engine把HttpServletResponse对象返回给Connector
13. Connector把HttpServletResponse对象返回给客户browser


### nio vs bio

NIO是一种同步非阻塞IO, 基于Reactor模型来实现的。其实相当于就是一个线程处理大量的客户端的请求，通过一个线程轮询大量的channel，每次就获取一批有事件的channel，然后对每个请求启动一个线程处理即可。这里的核心就是非阻塞，就那个selector一个线程就可以不停轮询channel，所有客户端请求都不会阻塞

nio代码:
```

@Test
    public void test1(){

        ServerSocketChannel socketChannelServer = null;
        try {
            socketChannelServer = ServerSocketChannel.open();
            socketChannelServer.socket().bind(new InetSocketAddress("127.0.0.1",1234));

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socketChannelServer.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Selector selector = null;

        try {
            selector = Selector.open();
            socketChannelServer.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("socketChannelServer1:"+socketChannelServer.hashCode());

        while (true){

            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();

                if(next.isAcceptable()){
                    try {
                        ServerSocketChannel ss =  (ServerSocketChannel)next.channel();

                        System.out.println("ServerSocketChannel2:"+ss.hashCode());


                        SocketChannel socketChannel = ss.accept();//有客户端进来，建立连接
                        socketChannel.configureBlocking(false);

//                        System.out.println(selector);selector==next.selector()
//                        System.out.println(next.selector());

                        socketChannel.register(selector,SelectionKey.OP_READ);//建立通道，处理read

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else if(next.isReadable()){
                    SocketChannel sc = null;
                    try {
                        sc =  (SocketChannel)next.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        byteBuffer.clear();


                        int offset = sc.read(byteBuffer);

                        if(offset!=-1){
                            String req = new String(byteBuffer.array(), 0, offset);
                            log.info("req:[{}]",req);
                        }

                        int responseCode = 200;

                        byte[] resb = "nice to meet you".getBytes();

                        String header = "HTTP/1.1 " + responseCode + "\r\n" +
                                "Server: NIO_SERVER_1.0\r\n" +
                                "Charset: UTF-8\r\n" +
//                                "Content-Type: " + mimeType + "\r\n" +
                                "Cache-Control: no-cache\r\n" +
                                "Access-Control-Allow-Origin: *\r\n" +
                                "Content-Length: " + resb.length + "\r\n\r\n";

                        ByteBuffer res = ByteBuffer.wrap(resb);
                        ByteBuffer headerBuffer = ByteBuffer.wrap(header.getBytes());

                        // 将响应头和资源数据一同返回
                        sc.write(new ByteBuffer[]{headerBuffer, res});






                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {

                        if(null!=sc){
                            try {
                                sc.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

```

IO所需要的CPU资源非常少。大部分工作是分派给DMA完成的。
DMA（Direct Memory Access）


### 几大问题

1.Tomcat线程模型如何实现，为何不用Netty？ 
2.Tomcat的NIO和NIO2有何区别？怎么选择？ 
3.Netty线程模型如何实现？
4.什么是EventLoop？什么是EventLoopGroup ？
5.Netty线程模型在使用中有什么需要注意的？ 
6.Netty的Channel如何处理的？线程安全吗？ 
7.Netty如何实现Java层面的零拷贝的？ 
8.Linux内核IO操作实现原理了解吗？ 
9.Linux内核如何实现零拷贝？
