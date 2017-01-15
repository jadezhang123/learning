var JADEZ = JADEZ || {};
JADEZ.namespace = function(ns_string){
	var parts = ns_string.split('.'),
		parent  = JADEZ,
		i;
	
	if (parts[0] === 'JADEZ') {
		parts = parts.slice(1);
	}

	for (i = 0; i < parts.length; i++) {
		if (typeof parent[parts[i]] === 'undefined') {
			parent[parts[i]] = {};
 		}
 		parent = parent[parts[i]];
	}
	return parent;
};

var module1 = JADEZ.namespace('JADEZ.modules.module1');
console.log(module1 === JADEZ.modules.module1);

function inherit(Child, Parent){
	Child.prototype = new Parent();
}

function Parent(name){
	this.name = name || 'parent';
}

Parent.prototype.sayName = function() {
	return this.name;
};

function Child(name){

}

inherit(Child, Parent);

var kid = new Child();
console.log(kid.sayName());
