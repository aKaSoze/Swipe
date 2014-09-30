package fractal.games.circus.sorin.petre.nica.async;

public class AsyncLong {

	private final EvaluationFunction	evaluationFunction;

	public AsyncLong(EvaluationFunction evaluationFunction) {
		this.evaluationFunction = evaluationFunction;
	}

	public void valueUpdated() {

	}

	public interface EvaluationFunction {
		Long evaluate();
	}

}
