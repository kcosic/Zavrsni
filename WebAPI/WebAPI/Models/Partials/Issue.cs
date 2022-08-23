using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{

    partial class Issue {

        public static IssueDTO ToDTO(Issue item, bool singleLevel = true)
        {
            return new IssueDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Summary = item.Summary,
                DateOfSubmission = item.DateOfSubmission,
                User = singleLevel ? null : User.ToDTO(item.User),
                UserId = item.UserId

            };
        }

        public static ICollection<IssueDTO> ToListDTO(ICollection<Issue> list, bool singleLevel = true)
        {
            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public IssueDTO ToDTO(bool singleLevel = true)
        {
            return new IssueDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Summary = Summary,
                DateOfSubmission = DateOfSubmission,
                User = singleLevel ? null : User.ToDTO(User),
                UserId = UserId

            };
        }


    }

}