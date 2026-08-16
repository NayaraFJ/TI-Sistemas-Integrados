# Sincronização do Trabalho Interdisciplinar

`TI/Trabalho` é a fonte de verdade do conteúdo acadêmico do projeto.

Sempre que um arquivo, uma imagem ou qualquer outro material em `TI/Trabalho` for criado, alterado, renomeado ou removido, a alteração correspondente deve ser refletida em `TI/Overleaf` na mesma tarefa. Depois da sincronização, o projeto LaTeX deve ser compilado e o PDF gerado deve ser verificado.

Para atualizar o conteúdo LaTeX a partir dos arquivos Markdown, execute:

```powershell
& '.\\scripts\\sync-trabalho.ps1'
```

a partir de `TI/Overleaf`. O arquivo `conteudo.tex` é gerado por esse processo e não deve receber alterações manuais persistentes.
