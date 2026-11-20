const fs = require('fs');
const path = require('path');
const root = __dirname;
const out = path.join(root, 'dist');
const files = ['index.html', 'styles.css', 'mock-data.js', 'core.js', 'app.js'];
if (fs.existsSync(out)) fs.rmSync(out, { recursive: true, force: true });
fs.mkdirSync(out, { recursive: true });
for (const file of files) {
  const source = path.join(root, file);
  if (!fs.existsSync(source)) throw new Error(`Arquivo ausente: ${file}`);
  fs.copyFileSync(source, path.join(out, file));
}
console.log(`Build estático concluído: ${files.length} arquivos em dist/`);
