package me.themallard.bitmmo.api.output.resource;

public class OutputResourceListFactory {
	private OutputResourceList list;

	public OutputResourceListFactory() {
		list = new OutputResourceList();
	}

	public OutputResourceListFactory add(OutputResource or) {
		list.add(or);
		return this;
	}

	public OutputResourceListFactory add(OutputResource... or) {
		list.addAll(or);
		return this;
	}

	public OutputResourceList build() {
		return list;
	}
}
