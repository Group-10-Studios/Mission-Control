class Josh {
	constructor(name, age, school="Victoria") {
		this.name = name;
		this.age = age;
		this.school = school;
	}

	addAge(toAdd) {
		this.age += toAdd;
	}
}

var p = new Josh("jake", 21);
p.addAge(2)

console.log("SCHOOL: " + p.school);
console.log("AGE: " + p.age);