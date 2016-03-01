# ForceLang-JS
A ForceLang module for using java's builtin javascript engine.

Once this module has been installed (which can be done by adding the source to your ForceLang build path, then recompiling), you can use it by simply putting `cons js require njs` at the top of your ForceLang code, then calling `js` as a function to execute arbitrary javascript one-liners, passed as barewords.

The returned values are automatically converted into the corresponding ForceLang types where possible.
