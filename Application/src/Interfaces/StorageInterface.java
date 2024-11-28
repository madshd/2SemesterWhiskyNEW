package Interfaces;

import Production.Distillate;
import Warehousing.Cask;

import java.util.List;

public interface StorageInterface {
    public List<Distillate> getDistillates();
    public void storeDistillate(Distillate distillate);
    public void deleteDistillate(Distillate distillate);

    public List<Cask> getCasks();
    public void storeCask(Cask cask);
    public void deleteCask(Cask cask);

}
