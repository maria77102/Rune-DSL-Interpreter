package com.regnosys.rosetta.generator.external;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.resource.Resource;

import com.regnosys.rosetta.generator.java.RosettaJavaPackages;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.rosetta.RosettaRootElement;
import com.rosetta.util.DemandableLock;


/**
 * Abstract base class with some skeleton implementation for external generators.
 * 
 * Implementors should subclass this class and provide their own concrete implementations of {@link #generate()}
 *  
 */
public abstract class AbstractExternalGenerator implements ExternalGenerator {

	private final String name;
	
	public AbstractExternalGenerator(String name) {
		this.name = name;
	}

	@Override
	public void generate(RosettaJavaPackages packages, List<RosettaRootElement> elements, String version,
			Consumer<Map<String, ? extends CharSequence>> processResults, Resource resource,
			DemandableLock generateLock) {
		Map<String, ? extends CharSequence> generate = generate(packages, elements, version);
		processResults.accept(generate);
	}

	
	@Override
	public void afterGenerate(List<RosettaModel> models, Consumer<Map<String, ? extends CharSequence>> processResults,
			Resource resource, DemandableLock generateLock) {
		Map<String, ? extends CharSequence> generate = afterGenerate(models);
		processResults.accept(generate);
	}

	@Override
	public ExternalOutputConfiguration getOutputConfiguration() {
		return new ExternalOutputConfiguration(name, "Code generation configuration");
	}
	
	/**
	 * Returns a map of {filename -> source code} for all the classes generated by the rosetta source
	 * 
	 * @param	packages a class that provides package and path structure
	 * @param 	elements the list of all rosetta elements
	 * @param 	version  the version of rosetta files
	 * @return
	 */
	public abstract Map<String, ? extends CharSequence> generate(RosettaJavaPackages packages, List<RosettaRootElement> elements, String version);

	public Map<String, ? extends CharSequence> afterGenerate(List<RosettaModel> models) {
		//By default don't do anything in the after generate step
		return Collections.emptyMap();
	}
}
