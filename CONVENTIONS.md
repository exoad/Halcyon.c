# Documentation | 文

This markdown document provides a simple overview of how things go around regarding usage & readability of source code (not this repo!).

## File Naming

File Naming, source suffixing, represents how most Java Source Files are named based on their intended purposes / category.

### JAVA Source Files

| <strong>Prefix</strong> | <strong>Description</strong> |
| :------: | :-----------: |
| <code>gui_</code> | Represents standalone GUI Components that depend on <code>dgui_</code> components to make a whole usable component. Also known as the boilerplate GUI Component |
| <code>dgui_</code> | Represents dependent GUI Components that require itself to be added to a <code>gui_</code> based component for it to work |
| <code>use_</code> | Defines general functionality; functionalities could either be all <code>static</code> or a usuable class defining an instance for functionality. Generally known as a usable. |
| <code>const_</code> | Defines constants, most often encountered as an <code>interface</code>. Should not define a constructor & functions (unless for additionally functionality to the constants) |
| <code>sys_</code> | A </code>use_</code> variation that clearly specifies that this class defines and/or uses functionality that are defined with <code>native</code> |
| <code>NONE</code> | When no prefix is encountered, it should be noted as a "Runner" class meaning it most likely defines a <code>public static void main</code> OR it could be a <code>use_</code> (or variations of it) that is used commonly: saves time typing |
| <code>impl_</code> | Defines a abstraction of <code>abstract class</code> OR <code>abstract interface</code> where certain functionality is abstracted and to be implemented. |
| <code>evnt_</code> | A <code>impl_</code> variation that clearly specifies that this abstraction is used for event handling, specifically for event callbacks |

### SCRIPT Files
| <strong>Prefix</strong> | <strong>Description</strong> |
| :------: | :-----------: |
| <code>run_</code> | Represents the entirety of most RUNNABLE script files, like BASH, POWERSHELL, etc.. Runnable meaning it is intended to be executed via a Shell by the user. Runnables are usually meant to be source files and not compiled |

### Locale Resource Files
| <strong>Prefix</strong> | <strong>Description</strong> |
| :------: | :-----------: |
| <code>HalcyonLang_</code> | A Halcyon Language Locale file. This prefix is most likely followed by the language code (e.g. "en_US") |
