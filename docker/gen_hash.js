const { execSync } = require('child_process');

// Install bcryptjs if not present
try {
  require('bcryptjs');
} catch(e) {
  console.log('Installing bcryptjs...');
  execSync('npm install bcryptjs', { cwd: __dirname, stdio: 'inherit' });
}

const bcrypt = require('bcryptjs');
const password = process.argv[2] || 'admin123';
const hash = bcrypt.hashSync(password, 10);
console.log('\nPassword:', password);
console.log('BCrypt hash:', hash);
console.log('\nVerification:', bcrypt.compareSync(password, hash));
