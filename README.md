**This project, using Java as its GUI framework, will be deprecated soon.** Java has served me well in terms of teaching me the very basics of creating a working UI, from the very first version I made using a single file in under a day learning about Java's internal builtin sound library to writing complete shader API's and a whole new sound library in C. Starting from having no particular interesting name, to MP4J (Music Player 4 Java), to finally Halcyon, with countless GUI redesigns and rewrites to make it better. With each rewrite, I added more, became more fluent with GUI building in Java, and tried my best to squeeze every last drop of performance to add  a tiny feature. Finally, the performance required and the overall throughput I need to put in from my side into Java and to only get small drop of performance is not worth it. Furthermore, the bootstrap and lack of resources within Java itself makes the GUI creation process extremely tedious and the lack luster ability to properly handle resolution scaling, accelerated images, and volatility makes the GUI building a very big hassle. Thus, I am proposing to rewrite this project entirely under Dart for the GUI, C/Rust for Audio, and Lua for interop.. I have always wanted to add more to the program, make it more eye catching and have a lot more eye candy to it, and I believe that by rewriting in a different framework, Flutter+Dart, I will be able to achieve performance above that of Java.

Lastly, the audio engine is not changing. The audio engine will stay in development using C and remain that way; however, the API essence for it to be used with third parties will dwindle.

Thank you Java and for anyone who has taken a slight interest in my pursuit! <3

<h1 align="center"> <strong>Halcyon: Robust & Extensible Music Player</strong><br><br><a href="https://halcyoninae.github.io/.github/"><img src="repo/img/github_banner.png" alt="Repository Banner" width="512"/></a></h1>
<hr>

**Update Stream:**<br>

```
---
Jack Meng (exoad) @ 1/20/2023 8:35 PM EST
This project is currently semi-dormant :( The reason being that I am currently working 
on [exoad/HaliteLoader] which promises to revamp the entirety
of Halcyon's current property system which is very janky and tedious to use. Check it out 
here: https://github.com/exoad/HaliteLoader.java
---
```

`Java Part Documentation (JavaDocs):` https://exoad.github.io/Halcyon.htm/ <br>
