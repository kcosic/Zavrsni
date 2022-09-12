﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{

    partial class Issue
    {

        public static IssueDTO ToDTO(Issue item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new IssueDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Summary = item.Summary,
                DateOfSubmission = item.DateOfSubmission,
                Request = singleLevel ? null : Request.ToDTO(item.Request),
                RequestId = item.RequestId

            };
        }

        public static ICollection<IssueDTO> ToListDTO(ICollection<Issue> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }

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
                Request = singleLevel ? null : Request.ToDTO(Request),
                RequestId = RequestId

            };
        }


    }

}