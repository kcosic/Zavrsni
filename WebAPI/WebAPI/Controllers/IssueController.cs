using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class IssueController : BaseController
    {
        public IssueController() : base(nameof(IssueController)) { }

        [HttpGet]
        [Route("api/Issue/{id}")]
        public BaseResponse RetrieveIssue(int id, bool expanded = false)
        {
            try
            {
                var issue = Db.Issues.Find(id);
                if (issue == null || issue.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(issue.ToDTO(!expanded));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/Issue/Request/{requestId}")]
        public BaseResponse RetrieveRequestIssues(int requestId)
        {
            try
            {
                var issue = Db.Issues.Where(x => x.RequestId == requestId && !x.Deleted).ToList();
                if (issue == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Issue.ToListDTO(issue));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/Issue/{id}")]
        public BaseResponse DeleteIssue(int id)
        {
            try
            {
                var issue = Db.Issues.Find(id);
                if (issue == null || issue.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                issue.DateDeleted = DateTime.Now;
                issue.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPost]
        [Route("api/Issue")]
        public BaseResponse CreateIssue(IssueDTO newIssueDTO)
        {
            try
            {
                if (newIssueDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var newIssue = new Issue
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    RequestId = newIssueDTO.RequestId,
                    Summary = newIssueDTO.Summary,
                    DateOfSubmission= newIssueDTO.DateOfSubmission,
                };

                Db.Issues.Add(newIssue);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Issue/{id}")]
        public BaseResponse UpdateIssue([FromUri] int id, [FromBody] IssueDTO issueDTO)
        {
            try
            {
                if (issueDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var issue = Db.Issues.Find(id);
                if (issue == null || issue.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                issue.DateModified = DateTime.Now;
                issue.DateOfSubmission = issueDTO.DateOfSubmission;
                issue.Summary = issueDTO.Summary;
                issue.RequestId = issueDTO.RequestId;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }
    }
}