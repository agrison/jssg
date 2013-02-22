# Jssg: Java Static Site Generator

Jssg is a simple static site generator like Jekyll, but with less features and written in Java.
The concept behind is the same as all static site generators.
The program takes a directory containing blog posts and site pages written in either `Markdown` or `Textile`, and merge the generated markup in a complete set of `HTML` files using `Freemarker`.
Then the site can be easily put on any web server, or even on GitHub.

## Getting Started

### Install Jssg

To get a copy of **jssg** just clone the git repository and `mvn package`.

    $ git clone git://github.com/agrison/jssg.git
    $ cd jssg
    $ mvn package
    -- add jsgg to your $PATH

### Init

To create a default site structure, just call `jssg init`.

    $ mkdir myblog
    $ cd myblog
    $ jssg -init

After the `init` command has finish to run, you should have the following structure:

    + _layout/
    |--+ index.html
    |--+ blog.html
    + _posts/
    |--+ yyyy-MM-dd-Hello.markdown
    |--+ 2011-11-20-Unix-Prompt.markdown
    + index.textile
    + config.properties

You can edit some of the properties in the `config.properties` file, for instance the path to `pygments`.

* A default index page content is written in the `index.textile` file, and its layout is written in `_layout/index.html`.
* A default blog post is written in the `_posts/yyyy-MM-dd-Hello.mkd` file, and its layout is written in `_layout/blog.html`.

### Write content

With the `jssg -init` command you have already a default index page and blog post, but you can add more if you want.

    $ cat <<EOF > _posts/2011-10-29-Hello.mkd
    ---
    title: Hello
    layout: blog
    description: Welcome
    date: 2011-10-29
    comments: true
    url: /2011/10/29/Hello
    ---

    Hello
    ==============
    Welcome to my blog.
    EOF

Write an about page.

    $ cat <<EOF > about.textile
    ---
    title: My awesome webpage
    layout: index
    description: About page
    ---

    h2. About

    I'm what's up in New York.

### Create your layout

If you need to customize the layouts, they are located in `_layouts`, below are two commandes that show what are the defaults
bundled in the sample.

    $ cat <<EOF > _layout/blog.html
    <!DOCTYPE html>
    <html>
       <head>
          <title>${header['title']}</title>
       </head>
       <body>
           <div class="content">
               <span>${header['date']?date?string.medium}</span>

               ${content}

               <#if header['comments']??>
               <div id="comments">
                   <div id="disqus_thread"></div>
                   <script type="text/javascript">
                     var disqus_shortname = '***';
                     var disqus_identifier = '/blog/${header['title']}';
                     var disqus_url = 'http://www.foo.com${header['url']}';
                     var disqus_developer = 1;
                     (function() {
                         var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
                         dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';
                         (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
                     })();
                   </script>
                   <noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
               </div>
               </#if>
           </div>
        </body>
    </html>

Create the index page:

    $ cat <<EOF > _layout/index.html
    <!DOCTYPE html>
    <html>
       <head>
          <title>Welcome to my website</title>
       </head>
       <body>
          <div class="content">
              ${content}

             <h2>Blog posts</h2>
             <ul>
             <#assign blogPosts = posts?sort_by("date")>
             <#foreach post in blogPosts?reverse>
                <li><span class="date">${post.date?date?string.medium}</span> &mdash; <a href="${post.url}">${post.title}</a></li>
             </#foreach>
             </ul>
          </div>
        </body>
    </html>

### Run jssg

Run the command `jssg -build -serve` to generate the whole site, then browse the site.

    $ jssg -build -serve
          .--.
        .--,`|   .--.--.      .--.--.      ,----._,.
        |  |.   /  /    '    /  /    '    /   /  ' /
        '--`_  |  :  /`./   |  :  /`./   |   :     |
        ,--,'| |  :  ;_     |  :  ;_     |   | .\  .
        |  | '  \  \    `.   \  \    `.  .   ; ';  |
        :  | |   `----.   \   `----.   \ '   .   . |
      __|  : '  /  /`--'  /  /  /`--'  /  `---`-'| |
    .'__/\_: | '--'.     /  '--'.     /   .'__/\_: |
    |   :    :   `--'---'     `--'---'    |   :    :
     \   \  /                              \   \  /
      `--`-'                                `--`-'

    Looking for posts in _posts/
      About to process 1 file
        + Processing 2011-10-29-Hello.markdown...

    Looking for pages in ./
      About to process 1 files
        + Processing index.textile...

    Site generated in 0.50 sec
    Starting the Jetty server at http://localhost:9876 ...

    $ curl http://localhost:9876/2011/10/29/Hello | grep Hello
    <title>Hello</title>
         <h1 id="hello">Hello</h1>
               var disqus_identifier = '/blog/Hello';
               var disqus_url = 'http://www.foo.com/2011/10/29/Hello';

    $ curl http://localhost:9876 | grep personal
    <p>Welcome to my personal web site. You will find some great stuff here.</p>

    $ open http://localhost:9876

When you run `jssg` with the `serve` argument, it rebuild any post or static page that you modify when it notices it,
so that you just have to refresh your browser.

## How it works

Invoking `jssg -build` will build the website by doing specific tasks:

* Scan the _posts/ directory for each file having the pattern yyyy-MM-dd-.something.(markdown|textile)
    * Detect code snippets (if any)
    * Generate the HTML markup equivalent for the `Markdown` or `Textile` file content
    * Highlight code snippets with (`Jygments` / `Pygments`)
    * Merge the HTML into the layout found in the `_layout/` directory using Freemarker
    * Write the whole content in `_build/yyyy/MM/dd/something`
* Scan the current directory for each file having the mkd|markdown|textile extension
    * Do exactly the same as above
* Copy everything that is in the current directory that has not already been processed into the _build/ directory.

All these tasks are being executed automatically and takes just seconds.

## Dependencies

`Jssg` supports code snippets higlighting through `Pygments`, so it should be in your `PATH` or you may set the path to it in the `config.properties` file.

Jssg is packaged as only one big fat JAR which includes all its dependencies. 
These dependencies are resolved through Maven or via the `libs` directory bundled with the source code:

* Spring
* Google Guava
* Markdown4J
* Textile4j
* Freemarker
* SnakeYaml
* Jsoup
* Jygments
* Logback
* Args4j
* Jetty

## Todos

* Refactoring of external formatters, to generate multiple outputs like `LaTeX`, `PDF` through `Flying Saucer`, ...
* More features
* Less bugs.