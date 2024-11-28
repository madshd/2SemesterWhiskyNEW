package Storage;

import Interfaces.StorageInterface;
import Production.Distillate;
import Warehousing.Cask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Storage implements StorageInterface, Serializable {
    private List<Distillate> distillates = new ArrayList<>();
    private List<Cask> casks = new ArrayList<>();

    @Override
    public List<Distillate> getDistillates() {
        return new ArrayList<>(distillates);
    }

    @Override
    public void storeDistillate(Distillate distillate) {
        distillates.add(distillate);
    }

    @Override
    public void deleteDistillate(Distillate distillate) {
        distillates.remove(distillate);
    }

    @Override
    public List<Cask> getCasks() {
        return new ArrayList<>(casks);
    }

    @Override
    public void storeCask(Cask cask) {
        casks.add(cask);
    }

    @Override
    public void deleteCask(Cask cask) {
        casks.remove(cask);
    }
}
