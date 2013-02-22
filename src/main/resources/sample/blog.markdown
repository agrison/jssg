---
title: Hello
layout: blog
description: Welcome
date: DATE1
comments: true
url: DATE2/Hello
---

Hello
==============
Welcome to my blog.

You can see that I like Java
{% highlight java %}
public class Foo {
   public static void main(String[] args) {
      System.out.println("Hello, world!");
   }
}
{% endhighlight %}

Python
{% highlight python %}
def foo():
    ''' Unleash the foo method '''
    print "Hello, world!"
{% endhighlight %}

and OCaml too
{% highlight ocaml %}
(** Some String utilities *)
module StrUtils =
struct
    (** Returns a list containing each line in the given string *)
    let lines_of str = Str.split (Str.regexp "\n") str
    (** Return a tuple where [fst] is the key and [snd] is the value in a
     string expression like ["key: value"] *)
    let header_parts line =
        let p = Str.split (Str.regexp ":") line
        in (List.hd p, List.nth p 1)
    (**
      trim a string by removing ["^[ \t\r\n]+"] and ["[ \t\r\n]+$"] from it.
      Example [trim "\thello\t \tworld !  \t" == "hello\t \tworld !"]
    *)
    let trim str = Str.global_replace (Str.regexp "^[ \t\r\n]+") "" (Str.global_replace (Str.regexp "[ \t\r\n]+$") "" str)
    (** Returns what is after a colon, in the given line *)
    let after_colon line = List.hd (List.rev (Str.split (Str.regexp_string ":") line))
    (** Get the String representation of a char code, even if it exceeds
     unsigned char limits. Works as C unsigned char casting. *)
    let safe_char i = String.make 1 (Char.chr (i mod 256))
end;;
{% endhighlight %}
