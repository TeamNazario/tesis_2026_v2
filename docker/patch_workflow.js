const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, '..', 'workflow-n8n', 'Chatbot WhatsApp IA - BIOFLUID.json');
const OLD_URL = 'https://31e6-186-148-196-21.ngrok-free.app';
const NEW_URL = 'https://mongoose-passing-ogle.ngrok-free.dev';

const content = fs.readFileSync(filePath, 'utf8');
const count = (content.split(OLD_URL).length - 1);
console.log('Ocurrencias encontradas:', count);

const updated = content.split(OLD_URL).join(NEW_URL);
fs.writeFileSync(filePath, updated, 'utf8');

const verify = (updated.split(OLD_URL).length - 1);
console.log('Ocurrencias antiguas restantes:', verify);
console.log('Reemplazos realizados:', count - verify);
console.log('Nueva URL aplicada:', NEW_URL);
