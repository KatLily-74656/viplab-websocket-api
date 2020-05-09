package de.uni_stuttgart.tik.viplab.websocket_api.amqp;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import de.uni_stuttgart.tik.viplab.websocket_api.ViPLabBackendConnector;
import de.uni_stuttgart.tik.viplab.websocket_api.model.Computation;
import de.uni_stuttgart.tik.viplab.websocket_api.model.ComputationTask;
import de.uni_stuttgart.tik.viplab.websocket_api.model.ComputationTemplate;
import de.uni_stuttgart.tik.viplab.websocket_api.transformation.ComputationMerger;

@ApplicationScoped
public class AMQPConnector implements ViPLabBackendConnector {

	@Inject
	@Channel("computations")
	Emitter<String> computations;

	@Inject
	ComputationMerger merger;

	private Jsonb jsonb = JsonbBuilder.create();

	@Override
	public CompletionStage<String> createComputation(ComputationTemplate template, ComputationTask task) {
		Computation computation = merger.merge(template, task);
		String computationJson = jsonb.toJson(computation);
		
		return computations.send(computationJson).thenApply(v -> {
			return computation.identifier;
		});
	}

}