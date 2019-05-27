package com.example.paper.service;

import com.example.paper.bl.PaperListService;
import com.example.paper.dao.PaperRepo;
import com.example.paper.dao.UserRepo;
import com.example.paper.entity.Paper;
import com.example.paper.entity.User;
import com.example.paper.response.BasicResponse;
import com.example.paper.response.PaperSimpleInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaperListServiceImpl implements PaperListService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PaperRepo paperRepo;

    @Override
    public BasicResponse addPaperToList(int paperId, String username){
        if(userRepo.findById(username).isPresent()) {
            User user = userRepo.findById(username).get();
            if(user.addPaper(paperId)){
                userRepo.save(user);
                return new BasicResponse(true,"");
            }else {
                return new BasicResponse(false,"该论文已被收藏！");
            }
        }else {
            return new BasicResponse(false,"该用户名不存在！");
        }
    }

    @Override
    public BasicResponse deletePaperFromList(int paperId, String username){
        if(userRepo.findById(username).isPresent()) {
            User user = userRepo.findById(username).get();
            if(user.deletePaper(paperId)){
                userRepo.save(user);
                return new BasicResponse(true,"");
            }else {
                return new BasicResponse(false,"该论文未被收藏！");
            }
        }else {
            return new BasicResponse(false,"该用户名不存在！");
        }
    }

    @Override
    public List<PaperSimpleInfoVO> getPaperListByUserName(String username){
        if(userRepo.findById(username).isPresent()) {
            List<PaperSimpleInfoVO> paperSimpleInfoVOS = new ArrayList<>();
            User user = userRepo.findById(username).get();
            ArrayList<Integer> paperIdList = user.getPaperIdList();
            for(int i=0;i<paperIdList.size();i++){
                if(paperRepo.findById(paperIdList.get(i)).isPresent()){
                    Paper paper = paperRepo.findById(paperIdList.get(i)).get();
                    paperSimpleInfoVOS.add(paper.getPaperSimpleInfoVO());
                }
            }
            return paperSimpleInfoVOS;
        }else {
            return null;
        }
    }

    @Override
    public int getPaperListCount(String username){
        if(userRepo.findById(username).isPresent()) {
            User user = userRepo.findById(username).get();
            return user.getPaperIdList().size();
        }else {
            return 0;
        }
    }
}
