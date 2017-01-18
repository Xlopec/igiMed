package maxoliynick.igimed.di;

import com.google.inject.AbstractModule;

import maxoliynick.igimed.core.repo.IStaffRepository;
import maxoliynick.igimed.core.repo.StaffRepository;
import maxoliynick.igimed.model.CreateEmplModel;
import maxoliynick.igimed.model.EditEmplModel;
import maxoliynick.igimed.model.ICreateEmplModel;
import maxoliynick.igimed.model.IEditEmplModel;
import maxoliynick.igimed.model.IMainModel;
import maxoliynick.igimed.model.MainModel;

public final class RoboBindings extends AbstractModule {

    @Override
    protected void configure() {
        bind(IStaffRepository.class).to(StaffRepository.class);
        bind(IMainModel.class).to(MainModel.class);
        bind(ICreateEmplModel.class).to(CreateEmplModel.class);
        bind(IEditEmplModel.class).to(EditEmplModel.class);
    }
}
