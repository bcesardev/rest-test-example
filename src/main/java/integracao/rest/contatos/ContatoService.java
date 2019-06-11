package integracao.rest.contatos;

import java.util.List;
import java.util.Optional;

import integracao.rest.contatos.exceptions.ContatoException;

public interface ContatoService {

	List<Contato> buscarContatos();

	Optional<Contato> buscarContato(Long id);

	Contato inserirOuAlterar(Contato contato) throws ContatoException;

	void remover(Long id);

}
