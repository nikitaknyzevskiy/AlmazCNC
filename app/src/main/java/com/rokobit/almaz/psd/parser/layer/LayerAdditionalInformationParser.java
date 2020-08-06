package com.rokobit.almaz.psd.parser.layer;

import com.rokobit.almaz.psd.parser.PsdInputStream;

import java.io.IOException;

public interface LayerAdditionalInformationParser {
	public void parse(PsdInputStream stream, String tag, int size) throws IOException;
}
