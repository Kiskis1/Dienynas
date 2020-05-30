package lt.viko.eif.dienynas.viewmodels;

import androidx.lifecycle.ViewModel;

import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.repositories.StorageRepository;
import lt.viko.eif.dienynas.utils.ApplicationData;

public class DestytojasViewModel extends ViewModel {

    public void addGroup(Group group){
        StorageRepository.getInstance().addGroup(group);
    }
    public void setDest(Destytojas dest){
        StorageRepository.getInstance().setDest(dest);
    }
    public void saveGrades(Group group){
        Destytojas dest = ApplicationData.getDestytojas();
        dest.getGroup().set((int)group.getId()-1,group);
        StorageRepository.getInstance().setDest(dest);
    }

}
