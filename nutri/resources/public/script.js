function mostrarResultado(obj) {
  const pre = document.getElementById("resultadoJson");
  const texto = JSON.stringify(obj, null, 2);
  pre.textContent = texto;
}

function tratarErro(err) {
  console.error(err);
  mostrarResultado({ erro: "Falha na requisição: " + err.message });
}

document.getElementById("btnCadastrarUsuario")
  .addEventListener("click", () => {
    const altura = document.getElementById("altura").value;
    const peso   = document.getElementById("peso").value;
    const idade  = document.getElementById("idade").value;
    const sexo   = document.getElementById("sexo").value;

    const url = "http://localhost:3003/usuario";
    const body = {
      altura: altura,
      peso:   peso,
      idade:  idade,
      sexo:   sexo
    };

    fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    })
    .then(resp => {
      if (!resp.ok) {
        return resp.text().then(texto => {
          throw new Error(texto || resp.status + " " + resp.statusText);
        });
      }
      return resp.json();
    })
    .then(json => mostrarResultado(json))
    .catch(err => {
      mostrarResultado({ erro: err.message });
    });
    
  });

document.getElementById("btnConsultarUsuario")
  .addEventListener("click", () => {
    const url = "http://localhost:3003/usuario";
    fetch(url)
      .then(resp => resp.json())
      .then(json => mostrarResultado(json))
      .catch(err => tratarErro(err));
  });

document.getElementById("btnRegistrarAlimento")
  .addEventListener("click", () => {
    const nome       = document.getElementById("alimentoNome").value;
    const quantidade = document.getElementById("alimentoQtd").value;

    const url = "http://localhost:3003/transacao/alimento";
    const body = {
      nome: nome,
      quantidade: quantidade
    };

    fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    })
    .then(resp => resp.json())
    .then(json => mostrarResultado(json))
    .catch(err => tratarErro(err));
  });

// 4) Registrar Exercício
document.getElementById("btnRegistrarExercicio")
  .addEventListener("click", () => {
    const nome    = document.getElementById("exercicioNome").value;
    const minutos = document.getElementById("exercicioMin").value;
    const peso    = document.getElementById("exercicioPeso").value;

    const url = "http://localhost:3003/transacao/exercicio";
    const body = {
      nome: nome,
      minutos: minutos,
      peso: peso
    };

    fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    })
    .then(resp => resp.json())
    .then(json => mostrarResultado(json))
    .catch(err => tratarErro(err));
  });

document.getElementById("btnConsultarExtrato")
  .addEventListener("click", () => {
    const dini = encodeURIComponent(document.getElementById("extratoIni").value);
    const dfim = encodeURIComponent(document.getElementById("extratoFim").value);
    const url = `http://localhost:3003/transacao/extrato?data-ini=${dini}&data-fim=${dfim}`;

    fetch(url)
      .then(resp => resp.json())
      .then(json => mostrarResultado(json))
      .catch(err => tratarErro(err));
  });

document.getElementById("btnConsultarSaldo")
  .addEventListener("click", () => {
    const dini = encodeURIComponent(document.getElementById("saldoIni").value);
    const dfim = encodeURIComponent(document.getElementById("saldoFim").value);
    const url = `http://localhost:3003/transacao/saldo?data-ini=${dini}&data-fim=${dfim}`;

    fetch(url)
      .then(resp => resp.json())
      .then(json => mostrarResultado(json))
      .catch(err => tratarErro(err));
  });
