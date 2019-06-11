package integracao.rest.contatos;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import integracao.rest.contatos.exceptions.ContatoException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ContatoServiceIntegrationTest {

	@Autowired
	private ContatoService service;

	@Autowired
	private ContatoRepository repository;

	private Contato contato;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void start() {
		contato = new Contato("Bruno", "0y", "9xxxxxxxxxx9");
	}

	@Test
	public void saveWithNomeNullShouldThrowContatoException() throws ContatoException {
		expectedException.expect(ContatoException.class);
		expectedException.expectMessage("O Nome deve ser preenchido");

		contato.setNome(null);
		service.inserirOuAlterar(contato);
	}

	@Test
	public void saveWithDddNullShouldThrowContatoException() throws ContatoException {
		expectedException.expect(ContatoException.class);
		expectedException.expectMessage("O DDD deve ser preenchido");

		contato.setDdd(null);
		service.inserirOuAlterar(contato);
	}

	@Test
	public void saveWithTelefoneNullShouldThrowContatoException() throws ContatoException {
		expectedException.expect(ContatoException.class);
		expectedException.expectMessage("O Telefone deve ser preenchido");

		contato.setTelefone(null);
		service.inserirOuAlterar(contato);
	}

	@Test
	public void saveShouldPersistContato() throws ContatoException {
		service.inserirOuAlterar(contato);
		List<Contato> result = service.buscarContatos();

		assertEquals(1, result.size());
		repository.deleteAll();
	}

	@Test
	public void deleteByIdShouldRemoveContato() throws ContatoException {
		service.inserirOuAlterar(contato);
		List<Contato> contatos = service.buscarContatos();
		assertEquals(1, contatos.size());

		service.remover(contato.getId());
		List<Contato> result = service.buscarContatos();
		assertEquals(0, result.size());
	}

	@Test
	public void mustUpdateEntitySuccessfully() throws ContatoException {
		service.inserirOuAlterar(contato);
		Optional<Contato> result = service.buscarContato(contato.getId());
		assertEquals("Bruno", result.get().getNome());

		contato.setNome("Bruno Teste 2");
		service.inserirOuAlterar(contato);
		Optional<Contato> resultWithNameChanged = service.buscarContato(contato.getId());
		assertEquals("Bruno Teste 2", resultWithNameChanged.get().getNome());
		repository.deleteAll();
	}
}
