package integracao.rest.contatos;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import integracao.rest.contatos.exceptions.ContatoException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContatoServiceIntegrationWithMockBeanTest {

	@MockBean
	private ContatoRepository repository;

	@Autowired
	private ContatoService service;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private final String nome = "Bruno";

	private final String ddd = "0y";

	private final String telefone = "9xxxxxxxxx9";

	private Contato contato;

	@Before
	public void start() {
		contato = new Contato(nome, ddd, telefone);
	}

	@Test
	public void saveWithNomeNullShouldThrowContatoException() throws ContatoException {
		expectedException.expect(ContatoException.class);
		expectedException.expectMessage("O Nome deve ser preenchido");

		Mockito.when(repository.save((Contato) Mockito.any()))
				.thenThrow(new ConstraintViolationException("O Nome deve ser preenchido", null));

		contato.setNome(null);
		service.inserirOuAlterar(contato);
	}

	@Test
	public void saveWithDddNullShouldThrowContatoException() throws ContatoException {
		expectedException.expect(ContatoException.class);
		expectedException.expectMessage("O DDD deve ser preenchido");

		Mockito.when(repository.save((Contato) Mockito.any()))
				.thenThrow(new ConstraintViolationException("O DDD deve ser preenchido", null));

		contato.setDdd(null);
		service.inserirOuAlterar(contato);
	}

	@Test
	public void saveWithTelefoneNullShouldThrowContatoException() throws ContatoException {
		expectedException.expect(ContatoException.class);
		expectedException.expectMessage("O Telefone deve ser preenchido");

		Mockito.when(repository.save((Contato) Mockito.any()))
				.thenThrow(new ConstraintViolationException("O Telefone deve ser preenchido", null));

		contato.setTelefone(null);
		service.inserirOuAlterar(contato);
	}

	@Test
	public void saveShouldPersistContato() throws ContatoException {
		service.inserirOuAlterar(contato);
		Mockito.verify(repository, Mockito.times(1)).save(contato);
	}

	@Test
	public void deleteByIdShouldRemoveContato() throws ContatoException {
		service.remover(1L);
		Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
	}

}
