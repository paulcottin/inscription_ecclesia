package org.ecclesiacantic.gui.result;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.text.DecimalFormat;

public class ResultValue {

    static private DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.00");

    private final DoubleProperty _fullFactor, _sommeEcartType;
    private final IntegerProperty _lessUsedNb, _noOccupiedPart;

    ResultValue(final double parFullFactor, final double parSommeEcartType, final int parLessUsedNb, final int parNoOccupiedPart) {
        _fullFactor = new SimpleDoubleProperty(parFullFactor);
        _sommeEcartType = new SimpleDoubleProperty(parSommeEcartType);
        _lessUsedNb = new SimpleIntegerProperty(parLessUsedNb);
        _noOccupiedPart = new SimpleIntegerProperty(parNoOccupiedPart);
    }

    private final double convertDouble(final DoubleProperty parV) {
        return Double.parseDouble(DOUBLE_FORMAT.format(parV.get()).replace(",", "."));
    }

    public double getFullFactor() {
        return convertDouble(_fullFactor);
    }

    public void setFullFactor(final double parFullFactor) {
        this._fullFactor.set(parFullFactor);
    }

    public double getSommeEcartType() {
        return convertDouble(_sommeEcartType);
    }

    public void setSommeEcartType(final double parSommeEcartType) {
        this._sommeEcartType.set(parSommeEcartType);
    }

    public int getLessUsedNb() {
        return _lessUsedNb.get();
    }

    public void setLessUsedNb(final int parLessUsedNb) {
        this._lessUsedNb.set(parLessUsedNb);
    }

    public int getNoOccupiedPart() {
        return _noOccupiedPart.get();
    }

    public void setNoOccupiedPart(final int parNoOccupiedPart) {
        this._noOccupiedPart.set(parNoOccupiedPart);
    }
}
