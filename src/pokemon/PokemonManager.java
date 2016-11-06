package pokemon;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pokemon.data.*;
import pokemon.ui.PokemonUI;

/**
 * The PokemonManager Class
 */
public class PokemonManager {
	/***/
	private static List<Pokemon> pokemons = new ArrayList<Pokemon>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		pokemons.add(new Pokemon("Seadra", Type.Water));
		pokemons.get(0).setTrainer(new Trainer("Ash", "Ketchum"));
		pokemons.add(1, new Pokemon("Nidorina", Type.Poison));
		pokemons.get(1).setTrainer(new Trainer("Aaron", "Aaronson"));
		pokemons.add(2, new Pokemon("Arcanine", Type.Fire));
		pokemons.get(2).setTrainer(new Trainer("Green", "Goblin"));
		// create a SWT window
		Display display = new Display();
		Shell shell = new Shell(display);
		PokemonUI pui;
		pui = new PokemonUI(shell, pokemons);
		pui.open();
		// TODO: initialize and open the PokemonUI right here !
	}
}
