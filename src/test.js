class Person {
	constructor(name, age, school="Victoria University of WELLINGTON") {
		this.name = name;
		this.age = age;
		this.school = school;
	}

	addAge(toAdd) {
		this.age += toAdd;
	}
}

var p = new Person("jake", 25);
p.addAge(4)

console.log("SCHOOL: " + p.school);
console.log("AGE: " + p.age);