# 通安 SDK Demo

本demo 为通安 Android SDK demo的演示项目。通过简单几步就可对接平台。


***

## 接入指南
 
### 使用Gradle方式:

#### 1.在Project的build.gradle文件中配置。

```
allprojects {
    repositories {
        //必须配置
        jcenter()
    }
}
```

#### 2.在App的build.gradle的文件中添加依赖。

```
dependencies {
        implementation 'com.tongan.study:tongan_learn_library:1.1.1'
}
```

## 开始使用



###  准备

**请先确保通过文档获取到学习URL**

### 使用 

#### 1.通过单利模式

建议采用此方法。

```
new StudyMessage.Builder()
.setStudyUrl("获取到的 URL") // 此方法为必要方法，我们通过它来实现对 URL 传递。(必要方法，必须调用)
.setStatusBarColor(statusBarColor) //设置状态栏颜色值，可根据实际情况自行更改。默认是"#F58609"。(非必要)
.setThemColor(themColor)//设置有关相机拍照页面 button 背景色。默认是"#F58609"。(非必要)
.builder().study(Activity); //Activity 上下文对象
```

#### 2.通过传统Intent方法调用

```
Intent intent = new Intent(this, StudyActivity.class);
intent.putExtra(TaConstant.TONGAN_LMS_URL, “获取到的 URL”);
intent.putExtra(TaConstant.TONGAN_LMS_STATUS,"设置状态栏颜色值");
intent.putExtra(TaConstant.TONGAN_LMS_THEM,"设置相机页面 button 颜色值");
startActivity(intent);
```




## 其他
愿你每天都有快乐的事可以分享。🐶

通安技术
c




