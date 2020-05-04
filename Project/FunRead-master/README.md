
## 趣阅

![](https://github.com/weidiezeng/FunRead/blob/master/image/1.PNG)
 
 趣阅是一款简洁阅读的app,目前只实现了新闻模块（api使用聚合数据api,免费）,采用 MVP + RxJava + Retrofit 架构的项目
  
 ## Statement
 本程序仅供学习交流, 不可用于任何商业用途
 
 ## Features
- 首页四大模块: 新闻 / CSDN/ 图片/天气(目前只实现了新闻)
- 实现首页的布局和数据的显示
- 实现自定义新闻栏目顺序
- 新闻详情页面支持日夜两种主题
- 自定义主题颜色 无缝切换日夜皮肤
- 内置3款 logo 随意互换
- 动态切换字体大小

## 
![](https://github.com/weidiezeng/FunRead/blob/master/image/2.PNG)
![](https://github.com/weidiezeng/FunRead/blob/master/image/3.PNG)
![](https://github.com/weidiezeng/FunRead/blob/master/image/4.PNG)
![](https://github.com/weidiezeng/FunRead/blob/master/image/5.PNG)
![](https://github.com/weidiezeng/FunRead/blob/master/image/6.PNG)
![](https://github.com/weidiezeng/FunRead/blob/master/image/7.PNG)

## Points
- 基本遵循 Material Design 设计风格(参照今日头条ui)
- 使用 [Google 官方 MVP 架构](https://github.com/googlesamples/android-architecture/tree/todo-mvp/) 
- V层基类的构建, 包括 BaseActivity 和 BaseFragment , 对外提供了相同的接口
- DrawerLayout + NavigationView + BottomNavigationView 搭配使用
- RxBus 代替 EventBus 进行组件之间通讯
- ViewPager 搭配 Fragment 懒加载
- SwipeRefreshLayout 搭配 RecyclerView 下拉刷新上拉加载
- 自定义 BottomNavigationBehavior 实现上滑隐藏下滑显示
- RxJava + Retrofit2 + OkHttp3 做网络请求
- OkHttp3 对网络返回内容做缓存, 还有日志、超时重连、头部消息的配置
- 使用原生的夜间模式
- 解决侧滑返回与 View 冲突问题
- 内置 2 款 Logo, 随意切换
- 使用 7.0 新工具 DiffUtil , 不再无脑 notifyDataSetChanged
- 使用 ItemTouchHelper 实现频道排序、频道移动, 参考 [ItemTouchHelperDemo](https://github.com/YoKeyword/ItemTouchHelperDemo)
- 使用 AutoDispose 绑定 RxJava 生命周期

## Changelog

2019-8-24
readmd 图片上传
新闻模块基本功能实现

2019-8-23
设置preference 实现

2019-8-19
新闻内容图片浏览(目前该功能已舍弃)

2019-8-17
新闻内容详情显示实现

2019-08-14
新闻界面详情完成

2019-08-13
聚合数据api开始使用

2019-08-11
添加栏目bug解决

2019-08-08
新闻主界面框架搭建

2019-08-07
基层接口搭建

2019-08-06
Initial commit

## TODO
模拟器运行查看新闻时卡顿bug(目前猜测由于聚合数据api限制，只能直接使用webview加载url（获取不到html源码）,从而导致内存剧增)

其他三大模块实现

webview无图模式

个人账户功能

## Libraries
- [Android Support Libraries](https://developer.android.com/topic/libraries/support-library/index.html)
- [Glide](https://github.com/bumptech/glide)
- [PhotoView](https://github.com/chrisbanes/PhotoView)
- [LicensesDialog](https://github.com/PSDev/LicensesDialog)
- [Retrofit](https://github.com/square/retrofit)
- [PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [RxLifecycle](https://github.com/trello/RxLifecycle)
- [OkHttp](https://github.com/square/okhttp)
- [Material Dialogs](https://github.com/afollestad/material-dialogs)
- [PermissionsDispatcher](https://github.com/hotchemi/PermissionsDispatcher)
- [Stetho](https://github.com/facebook/stetho)
- [MultiType](https://github.com/drakeet/MultiType)
- [Slidr](https://github.com/r0adkll/Slidr)
- [ButterKnife](https://github.com/JakeWharton/butterknife)

## License
```
Copyright 2019 weidiezeng

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
