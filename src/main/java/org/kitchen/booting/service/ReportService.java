//package org.kitchen.booting.service;
//
//import org.kitchen.booting.domain.Recipe;
//import org.kitchen.booting.repository.RecipeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class RecipeService {
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    public List<Recipe> findAll(){
//        List<Recipe> recipes = new ArrayList<>();
//        recipeRepository.findAll().forEach(e->recipes.add(e));
//        return recipes;
//    }
//
////    public List<RecipeVO> findByUno(Long uno)
////    {
//////        Optional<RecipeVO> recipe = recipeRepository.findByUno(uno);
////        List<RecipeVO> recipe = recipeRepository.findByUno(uno);
////        return recipe;
////    }
//
////    public List<RecipeVO> findByTitleLike(String keyword)
////    {
////        List<RecipeVO> recipes = new ArrayList<>();
////        recipeRepository.findByTitleLike(keyword).forEach(e->recipes.add(e));
////        return recipes;
////    }
////
////    public Optional<RecipeVO> findByRno(Long rno)
////    {
//////        Optional<RecipeVO> recipe = recipeRepository.findByUno(uno);
////        Optional<RecipeVO> recipe = recipeRepository.findByRno(rno);
////        return recipe;
////    }
////
////    public void deleteByRno(Long rno)
////    {
////        recipeRepository.deleteById(rno);
////    }
//
//    public Recipe save(Recipe recipe) {
//        recipeRepository.save(recipe);
//        return recipe;
//    }
//
//    @Transactional
//    public void deleteRecipe(Long recipeNo){
//        recipeRepository.deleteByRecipeNo(recipeNo);
//    }
//
//}
package org.kitchen.booting.service;

import org.kitchen.booting.domain.Category;
import org.kitchen.booting.domain.Profile;
import org.kitchen.booting.domain.Recipe;
import org.kitchen.booting.domain.Report;
import org.kitchen.booting.repository.ProfileRepository;
import org.kitchen.booting.repository.RecipeRepository;
import org.kitchen.booting.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POST_COUNT = 10; // 한 페이지에 존재하는 게시글 수

    @Transactional
    public List<Report> getReportList(Integer pageNum)
    {
        Page<Report> page = reportRepository.findAll(PageRequest
                .of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "reportDate")));
        List<Report> reports = page.getContent();
        List<Report> reportList = new ArrayList<>();

        for(Report report : reports)
        {
            reportList.add(report);
        }
        return reportList;
    }
    public Integer[] recipePageList(Integer curPageNum)
    {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getReportCount());

        // 총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;

        // 페이지 번호 할당
        for(int val=curPageNum, i=0; val<=blockLastPageNum; val++, i++) { pageList[i] = val; }

        return pageList;
    }

    @Transactional
    public Long getReportCount(){
        return reportRepository.count();
    }
}