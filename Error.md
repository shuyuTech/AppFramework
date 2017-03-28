Android Studio编译不了出现如下

```
Error:Could not find property 'compile' on org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler_Decorated@1b3472dc.
```
根据上面的提示，我们找到并且打开了build.gradle文件，
这时我们会发现其中内容变得没有格式(混乱)，配置文件加载顺序颠倒等问题。该项目中我们调整android和dependencies模块的位置(Android在前)完成后直接编译。OK！