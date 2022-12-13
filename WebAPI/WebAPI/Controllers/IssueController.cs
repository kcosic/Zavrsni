using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models;
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
                var issueDTO = issue.ToDTO(expanded ? 3 : 2);
                //if (expanded)
                //{

                //    issueDTO.Shop.Location = issue.Shop.Location.ToDTO();
                //    issueDTO.User = issue.User.ToDTO();
                //}

                return CreateOkResponse(issueDTO);
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
        [Route("api/Issue/{requestId}")]
        public BaseResponse RetrieveIssues(int requestId)
        {
            try
            {
                var issue = Db.Issues.Where(x => x.RequestId == requestId && !x.Deleted).ToList();
                if (issue == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Issue.ToListDTO(issue, 2));
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
        public BaseResponse CreateIssue(Issue newIssueDTO)
        {
            try
            {
                if (newIssueDTO == null)
                {
                    throw new ArgumentNullException();
                }

                var newIssue = new Issue
                {
                    DateCreated = DateTime.Now,
                    DateModified = DateTime.Now,
                    Price= newIssueDTO.Price,
                    Accepted= newIssueDTO.Accepted,
                    Description = newIssueDTO.Description,
                    RequestId = newIssueDTO.RequestId
                };

                Db.Issues.Add(newIssue);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (ArgumentNullException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.InvalidParameter);
            }            
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/Issue/{id}")]
        public BaseResponse UpdateIssue([FromUri] int id, [FromBody] Issue issueDTO)
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
                issue.DateCreated = DateTime.Now;
                issue.DateModified = DateTime.Now;
                issue.Price = issueDTO.Price;
                issue.Accepted = issueDTO.Accepted;
                issue.Description = issueDTO.Description;
                issue.RequestId = issueDTO.RequestId;

                Db.SaveChanges();

                return CreateOkResponse(issue.ToDTO(3));
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