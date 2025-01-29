const form = document.getElementById('medication-form');
const reminderList = document.getElementById('reminder-list');
const reminderPopup = document.getElementById('reminder-popup');
const reminderMessage = document.getElementById('reminder-message');
const reminderSound = document.getElementById('reminder-sound');

let reminders = [];

// Event listener for form submission
form.addEventListener('submit', function(event) {
    event.preventDefault();
    const medicationName = document.getElementById('medication-name').value;
    const medicationDatetime = document.getElementById('medication-datetime').value;
    const medicationDosage = document.getElementById('medication-dosage').value;
    const weeklyFrequency = document.getElementById('weekly-frequency').value;

    const reminder = {
        id: Date.now(),
        name: medicationName,
        datetime: medicationDatetime,
        dosage: medicationDosage,
        frequency: weeklyFrequency
    };

    reminders.push(reminder);
    displayReminders();
    form.reset();
});

// Function to display reminders in the list
function displayReminders() {
    reminderList.innerHTML = '';
    reminders.forEach(reminder => {
        const li = document.createElement('li');
        li.innerHTML = `${reminder.name} - ${reminder.datetime} - ${reminder.dosage} 
            <button class="edit-button" onclick="editReminder(${reminder.id})">Edit</button> 
            <button class="remove-button" onclick="removeReminder(${reminder.id})">Remove</button>`;
        reminderList.appendChild(li);
    });
}

// Function to edit a reminder
function editReminder(id) {
    const reminder = reminders.find(r => r.id === id);
    document.getElementById('medication-name').value = reminder.name;
    document.getElementById('medication-datetime').value = reminder.datetime;
    document.getElementById('medication-dosage').value = reminder.dosage;
    document.getElementById('weekly-frequency').value = reminder.frequency;
    removeReminder(id);
}

// Function to remove a reminder
function removeReminder(id) {
    reminders = reminders.filter(reminder => reminder.id !== id);
    displayReminders();
}

function closeReminder() {
    reminderPopup.style.display = 'none';
}

function showReminder(message) {
    reminderMessage.textContent = message;
    reminderPopup.style.display = 'flex';
    reminderSound.currentTime = 0;
    reminderSound.play().catch(error => {
        console.error("Audio playback failed:", error);
    });
}

setInterval(() => {
    const now = new Date();
    reminders.forEach(reminder => {
        const reminderTime = new Date(reminder.datetime);
        if (now >= reminderTime) {
            showReminder(`It's time to take your medication: ${reminder.name}`);
            removeReminder(reminder.id);
        }
    });
}, 600);
