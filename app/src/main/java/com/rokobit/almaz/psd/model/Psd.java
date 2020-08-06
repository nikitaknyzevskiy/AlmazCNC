package com.rokobit.almaz.psd.model;

import com.rokobit.almaz.psd.parser.layer.LayerParser;

import java.io.File;
import java.io.IOException;

public class Psd extends AbstractPsd<Layer> {

    public Psd(File psdFile) throws IOException {
        super(psdFile);
    }

    @Override
    protected Layer createLayer(LayerParser parser) {
        return new Layer(parser);
    }

}
