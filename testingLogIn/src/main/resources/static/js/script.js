function toggleSidebar() {
  let sidebar = document.getElementById("sidebar");
  let content = document.getElementById("content");

  if (sidebar.classList.contains("collapsed-sidebar")) {
    sidebar.classList.remove("collapsed-sidebar");
    content.classList.remove("collapsed-content");
    content.style.marginLeft = "320px";
  } else {
    sidebar.classList.add("collapsed-sidebar");
    content.classList.add("collapsed-content");
    content.style.marginLeft = "0";
  }
}

// Function to toggle the dropdown menu
function toggleDropdown(id) {
  const dropdownMenu = document.getElementById(id);

  // Close all other dropdowns before toggling the clicked one
  document.querySelectorAll(".dropdown-content").forEach((dropdown) => {
    if (dropdown.id !== id) {
      dropdown.classList.remove("show");
    }
  });

  // Toggle the selected dropdown
  dropdownMenu.classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function (event) {
  if (
    !event.target.closest(".dropdown-toggle") &&
    !event.target.closest(".dropdown-content")
  ) {
    document.querySelectorAll(".dropdown-content").forEach((dropdown) => {
      dropdown.classList.remove("show");
    });
  }
};

function toggleSubMenu(submenuId, event) {
    event.preventDefault(); // Prevent default anchor behavior

    let submenu = document.getElementById(submenuId);
    let arrowIconImg = submenu.previousElementSibling.querySelector(".arrow-icon img");
    let isOpen = submenu.classList.contains("open");

    // Close all unrelated submenus
    document.querySelectorAll(".submenu").forEach((otherSubmenu) => {
        if (!submenu.contains(otherSubmenu) && otherSubmenu !== submenu && !otherSubmenu.contains(submenu)) {
            otherSubmenu.classList.remove("open");

            // Reset arrow icons for closed submenus
            let otherArrowIcon = otherSubmenu.previousElementSibling?.querySelector(".arrow-icon img");
            if (otherArrowIcon) {
                otherArrowIcon.src = "../static/images/icons/arrow-down.png";
            }
        }
    });

    // Toggle the clicked submenu
    submenu.classList.toggle("open", !isOpen);
    arrowIconImg.src = isOpen
        ? "../static/images/icons/arrow-down.png"  // Change back to down arrow if closing
        : "../static/images/icons/greater-than.png"; // Change to right arrow if opening

    // **🔹 Save submenu state only if it is a submenu of a submenu**
    let allOpenSubmenus = [...document.querySelectorAll(".submenu.open")].map(sub => sub.id);
    localStorage.setItem("openedSubmenus", JSON.stringify(allOpenSubmenus));
}

function clearSubmenus() {
    localStorage.removeItem("openedSubmenus"); // Clear saved submenus
    document.querySelectorAll(".submenu").forEach(submenu => submenu.classList.remove("open"));
    document.querySelectorAll(".arrow-icon img").forEach(img => img.src = "../static/images/icons/arrow-down.png");
}






