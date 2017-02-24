var http = require('http');
var fs = require('fs');
var path = require('path');
var mime = require('mime');
var cache = {};

//文件不存在时发送404错误
function send404(response) {
	response.writeHead(404, {'Content-Type':'text/plain'});
	response.write('Error, 404: response not found');
	response.end();
}

//提供文件数据服务。
function sendFile(response, filePath, fileContents) {
	response.writeHead(200, {'Content-Type':mime.lookup(path.basename(filePath))});

	response.end(fileContents);

}

//将初次读取的静态文件缓存
function serveStatic(response, cache, absPath) {
	if (cache[absPath]) {
		sendFile(response, absPath, cache[absPath]);
	} else{
		fs.exists(absPath, function (exists) {
			if (exists) {
				//从硬盘中读取文件
				fs.readFile(absPath, function (err, data) {
					if (err) {
						send404(response);
					}else{
						cache[absPath] = data;
						sendFile(response, absPath, data);
					}
				});
			}else{
				console.log('file not found');
				send404(response);
			}
		});
	}
}

var server = http.createServer(function (request, response) {
	var filePath = false;
	if (request.url == '/') {
		filePath = 'public/index.html';
	}else{
		filePath = 'public' + request.url;
	}
	var absPath = './' + filePath;
	console.log('absPath:'+absPath);
	serveStatic(response, cache, absPath);
});

server.listen(3000, function () {
	console.log('Service listening on port 3000.');
});