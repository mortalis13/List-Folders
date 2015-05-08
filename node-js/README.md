
# List Folders (Node.js version)


## Requirements

1. MySQL server running and database `list_folders_node` created (use `add/db.sql` file to create database with needed structure).
2. [Node.js](http://nodejs.org/) to start the HTTP server.
3. To export folder structure `export` folder and all its subfolders should exist.


## Run

1. Open command line.
2. Enter `cd list-folders`.
3. Enter `npm install`.
4. Enter `node q`.
5. Wait until the message "Listening to port 3000 ..." appears.
6. Open URL in the browser `localhost:3000`.
7. The page with form should appear.


## Node Dependencies

1. [jquery](https://www.npmjs.com/package/jquery)
2. [jsdom](https://www.npmjs.com/package/jsdom)
3. [mysql](https://www.npmjs.com/package/mysql)
4. [open](https://www.npmjs.com/package/open)

## Problems

a) If the following error occurs:

    Unexpected token ILLEGAL
    
on `node q` command when the server starts and it's related to the `jsdom.js` file which creates `throw` statement

    throw new RangeError(`Invalid parsingMode option

then go to `node_modules/jsdom/lib`, open `jsdom.js` and replace backticks with single quotes.  


b) PHP version problems with encoding (russian/spanish) seem to not appear in Node.js version.

