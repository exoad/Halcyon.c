# <strong>Localization</strong>

> <em>the process of making something local in character or restricting it to a particular place</em>

<!--
    LICENSED UNDER VENDOR LICENSE
    SEE ./LICENSE

    Note: This documentation file
    is not finalized and is subject
    to constant change

    COPYRIGHT © Jack Meng 2021
-->

Localization is the means in which this API has certain aspects that are hardcoded in. For example, pre-handled error loggings are hard coded into many classes. Whether you like it or not, localization enables for the ease of plugin interfacing with the bundled GUI wrapper.

<hr>

## <strong><u>Pre-Handled Error Logging</u></strong>

This is the most common example you can find of localization. Pre-Handling allows for quick debugging and error logging for the GUI wrapper but may not be feasible for you. This in turn can cause unwanted file creation and irrational console IO.

All pre-handled error logging are queued in class <code>ExternalResource.dispatchLog(Exception...)</code>

<details close>
<summary><code>ExternalResource.dispatchLog</code></summary>

```java
        long start = System.currentTimeMillis();
        ExecutorService dispatch = Executors.newFixedThreadPool(1);
        Future<Void> writableTask = dispatch.submit(() -> {
            StringBuilder sb = new StringBuilder();
            for (Exception e : ex) {
                sb.append(e.toString());

                Date d = new Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                StringBuilder sbt = new StringBuilder();
                for (StackTraceElement ste : e.getStackTrace()) {
                    sbt.append(ste.toString()).append("\n");
                }
                writeLog("log",
                    "Halcyon/MP4J - LOG EXCEPTION | PLEASE KNOW WHAT YOU ARE DOING\nException caught time: " + df.format(d)
                        + "\n"
                        + e.getClass() + "\n" + e + "\n" +
                        e.getMessage() + "\nLOCALIZED: " + e.getLocalizedMessage() + "\n==BEGIN_STACK_TRACE==\n"
                        + sbt + "\n==END_STACK_TRACE==\n"
                        + "Submit an issue by making a PR to the file BUGS at " + DefaultManager.PROJECT_GITHUB_PAGE);
            }
            return null;
        });
        while (!writableTask.isDone()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        Debugger.log("LOG > Log dispatch finished in " + (end - start) + "ms.");
```

</details>

## <strong><u>Self-Localization</u></strong>

Sometimes you might want these localized features in your program (or maybe you just don't care), this is where self-localization comes into play.

There are two main ways to make this work, and all will do the same thing. The end result being a fully initialized <code>/halcyon/</code> directory with proper subdirectories:

- <code>log</code>
- <code>bin</code>

### **Method #1**

This method involves an overlooking method <code>halcyon.runtime.Program.localize()</code>. It provides full handling of the subdirectory creation and also creates an empty properties file (<code>conf.halcyon</code>). However, it will not enable any control over what is created and handled; for example, if the localization folder already exists, it will be overwritten.

### **Method #2**

This method is simply copying the startup process of <code>Halcyon.main(String...)</code> which is the following template:

```java
if (args.length > 0) {
  if (args[0].equals("-debug")) {
    DefaultManager.DEBUG_PROGRAM = true;
    Debugger.DISABLE_DEBUGGER = false;
    }
}

ExternalResource.checkResourceFolder(
  ProgramResourceManager.PROGRAM_RESOURCE_FOLDER);

for (String str : ProgramResourceManager.RESOURCE_SUBFOLDERS) {
    ExternalResource.createFolder(str);
}

ExternalResource.pm.checkAllPropertiesExistence();
```
