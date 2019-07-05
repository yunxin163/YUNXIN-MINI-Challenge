package xyh.lixue.searchProblem.service;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.stereotype.Component;
import xyh.lixue.searchProblem.entity.Problem;

import java.util.List;


@Component
public interface ProblemRepository extends ElasticsearchRepository<Problem,String> {
}
