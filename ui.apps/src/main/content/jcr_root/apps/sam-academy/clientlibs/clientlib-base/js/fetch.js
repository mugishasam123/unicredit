var btn = document.getElementById("click");
console.log(btn)
fetch("/bin/custom/path")
.then((res) => res.json())
.then((data) => console.log(data));

btn.addEventListener("click", () => {
  fetch("/bin/custom/path")
    .then((res) => res.json())
    .then((data) => console.log(data));

  console.log("hello");
});
