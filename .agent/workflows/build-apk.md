---
description: 构建 APK 的默认方式
---

# 构建 APK

// turbo-all

默认情况下，构建 **release** 版本的 APK，除非用户特别指定需要 debug 版本。

## 构建命令

```bash
cd /Users/yiyang/Desktop/BiliPai2.0
./gradlew assembleRelease
```

## APK 输出路径

Release: `app/build/outputs/apk/release/app-release.apk`
Debug: `app/build/outputs/apk/debug/app-debug.apk`

## 注意事项

- Release 构建需要签名配置
- 如果没有配置签名，会生成未签名的 APK
