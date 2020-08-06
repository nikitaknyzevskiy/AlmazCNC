package com.rokobit.almaz.psd.parser;

import com.rokobit.almaz.psd.parser.header.HeaderSectionParser;
import com.rokobit.almaz.psd.parser.imagedata.ImageDataSectionParser;
import com.rokobit.almaz.psd.parser.imageresource.ImageResourceSectionParser;
import com.rokobit.almaz.psd.parser.layer.LayersSectionParser;

import java.io.IOException;
import java.io.InputStream;

public class PsdFileParser {
    private HeaderSectionParser headerParser;
    private ColorModeSectionParser colorModeSectionParser;
    private ImageResourceSectionParser imageResourceSectionParser;
    private LayersSectionParser layersSectionParser;
    private ImageDataSectionParser imageDataSectionParser;

    public PsdFileParser() {
        headerParser = new HeaderSectionParser();
        colorModeSectionParser = new ColorModeSectionParser();
        imageResourceSectionParser = new ImageResourceSectionParser();
        layersSectionParser = new LayersSectionParser();
        imageDataSectionParser = new ImageDataSectionParser(headerParser.getHeader());
    }

    public HeaderSectionParser getHeaderSectionParser() {
        return headerParser;
    }

    public ImageResourceSectionParser getImageResourceSectionParser() {
        return imageResourceSectionParser;
    }

    public LayersSectionParser getLayersSectionParser() {
        return layersSectionParser;
    }
    
    public ImageDataSectionParser getImageDataSectionParser() {
        return imageDataSectionParser;
    }

    public void parse(InputStream inputStream) throws IOException {
        PsdInputStream stream = new PsdInputStream(inputStream);
        headerParser.parse(stream);
        colorModeSectionParser.parse(stream);
        imageResourceSectionParser.parse(stream);
        layersSectionParser.parse(stream);
        imageDataSectionParser.parse(stream);
    }
}
