package com.example.testingLogIn.Services;

import com.example.testingLogIn.CustomObjects.StudentTotalDiscount;
import com.example.testingLogIn.Models.Discount;
import com.example.testingLogIn.Models.Student;
import com.example.testingLogIn.AssociativeModels.StudentDiscount;
import com.example.testingLogIn.Repositories.DiscountRepo;
import com.example.testingLogIn.Repositories.StudentDiscountRepo;
import com.example.testingLogIn.Repositories.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountsServices {

    private final DiscountRepo discRepo;
    private final StudentDiscountRepo studDiscRepo;
    private final StudentRepo studRepo;

    public DiscountsServices(DiscountRepo discRepo, StudentDiscountRepo studDiscRepo, StudentRepo studRepo) {
        this.discRepo = discRepo;
        this.studDiscRepo = studDiscRepo;
        this.studRepo = studRepo;
    }

    public List<Discount> getDiscountsList(boolean isNotDeleted){
        if(isNotDeleted)
            return discRepo.findByIsNotDeletedTrue().stream().peek(discount -> discount.setPercentageDiscount(100*discount.getPercentageDiscount())).toList();
        return discRepo.findByIsNotDeletedFalse();
    }

    public boolean addDiscount(Discount discount){
        if(!discRepo.findDiscountByName(discount.getDiscountName().trim()).isEmpty())
            return false;
        discount.setNotDeleted(true);
        discRepo.save(discount);

        return true;
    }

    public void deleteDiscount(int discountId){
        Discount discount = discRepo.findById(discountId).orElseThrow(NullPointerException::new);
        discount.setNotDeleted(false);
        discRepo.save(discount);
    }

    public boolean addStudentDiscount(int discountId, List<Integer> studentIds){
        Discount discount = discRepo.findById(discountId).orElse(null);
        assert discount != null;
        studentIds.forEach(studentId -> {
            if(studDiscRepo.findStudentDiscountRecord(studentId,discountId).isEmpty()){
                Student student = studRepo.findById(studentId).orElse(null);
                studDiscRepo.save(StudentDiscount.builder()
                        .discount(discount)
                        .student(student)
                        .isNotDeleted(true)
                        .build());
            }
        });
        return true;
    }

    public StudentTotalDiscount getStudentTotalDiscount(int studentId){
        StudentTotalDiscount std= studDiscRepo.getStudentTotalDiscount(studentId).orElse(new StudentTotalDiscount(null,null));
        return std;
    }
}
