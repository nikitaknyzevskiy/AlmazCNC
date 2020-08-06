package com.rokobit.almaz.psd.parser.layer.additional;

import com.rokobit.almaz.psd.parser.PsdInputStream;
import com.rokobit.almaz.psd.parser.layer.LayerAdditionalInformationParser;
import com.rokobit.almaz.psd.parser.object.PsdDescriptor;

import java.io.IOException;

public class LayerVogkParser implements LayerAdditionalInformationParser {

	public static final String TAG = "vogk";
	private final LayerVogkHandler handler;
	
	public LayerVogkParser(LayerVogkHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void parse(PsdInputStream stream, String tag, int size) throws IOException {
		int eff = stream.readInt();
                int version = stream.readInt();
                PsdDescriptor descriptor = new PsdDescriptor(stream);
		if (handler != null) {
			handler.layerVogkParsed(descriptor);
		}
	}

}
