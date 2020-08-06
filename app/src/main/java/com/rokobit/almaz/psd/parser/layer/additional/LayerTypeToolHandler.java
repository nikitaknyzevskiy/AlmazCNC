package com.rokobit.almaz.psd.parser.layer.additional;

import com.rokobit.almaz.psd.parser.object.PsdDescriptor;

public interface LayerTypeToolHandler {

	public void typeToolTransformParsed(Matrix transform);
	public void typeToolDescriptorParsed(int version, PsdDescriptor descriptor);

}
