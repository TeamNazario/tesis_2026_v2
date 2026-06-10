const fs = require('fs');
const OLD_HASH_1 = '$2a$10$/Yzy6Mlg9XQpId5unjsPPeOS8gAPZaToX2K2vBcj4rx0EGRRUgtvG';
const OLD_HASH_2 = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVKMq1hpkG';
const NEW_HASH   = '$2b$10$sHhaiT78ABpM1254tW1VD.86jvF71q256jCVtPP14Y510qpYo4EM6';
// NEW_HASH corresponds to password: admin123

let content = fs.readFileSync('docker/mysql/init.sql', 'utf8');
let count = 0;

[OLD_HASH_1, OLD_HASH_2].forEach(old => {
  const escaped = old.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  const matches = (content.match(new RegExp(escaped, 'g')) || []).length;
  if (matches > 0) {
    content = content.split(old).join(NEW_HASH);
    count += matches;
  }
});

fs.writeFileSync('docker/mysql/init.sql', content, 'utf8');
console.log(`Updated ${count} password hash occurrences in init.sql`);
console.log('Password for all users: admin123');
